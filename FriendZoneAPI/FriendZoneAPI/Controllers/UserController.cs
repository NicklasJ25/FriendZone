using FriendZoneAPI.Models;
using System.Collections.Generic;
using System.Linq;
using System.Data.Entity;
using System.Web.Http;

namespace FriendZoneAPI.Controllers
{
    public class UserController : ApiController
    {
        FriendZoneEntities database = new FriendZoneEntities();

        // GET api/user
        public List<User> Get()
        {
            List<User> users = database.User.ToList();
            return users;
        }

        // GET api/user/email
        public User Get(string email)
        {
            User user = database.User.Find(email);
            return user;
        }

        // POST api/user
        public void Post([FromBody]User user)
        {
            database.User.Add(user);
            database.SaveChanges();
        }

        // PUT api/user/email
        public void Put(string email, [FromBody]User newUser)
        {
            User oldUser = database.User.Find(email);
            oldUser.Email = newUser.Email;
            oldUser.Firstname = newUser.Firstname;
            oldUser.Lastname = newUser.Lastname;
            oldUser.Birthday = newUser.Birthday;
            oldUser.Phone = newUser.Phone;
            oldUser.Streetname = newUser.Streetname;
            oldUser.Postalcode = newUser.Postalcode;
            oldUser.ProfilePicture = newUser.ProfilePicture;
            oldUser.Password = newUser.Password;
            oldUser.Partner = newUser.Partner;
            database.SaveChanges();
        }

        // DELETE api/user/email
        public void Delete(string email)
        {
            database.User.Remove(database.User.SingleOrDefault(u => u.Email.Equals(email)));
            database.SaveChanges();
        }
    }
}
